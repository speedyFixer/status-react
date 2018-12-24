(ns status-im.ui.screens.wallet.send.subs
  (:require [re-frame.core :as re-frame]
            [status-im.utils.money :as money]
            [status-im.models.wallet :as models.wallet]
            [status-im.utils.ethereum.core :as ethereum]
            [status-im.constants :as constants]))

(re-frame/reg-sub
 ::send-transaction
 :<- [:wallet]
 (fn [wallet]
   (:send-transaction wallet)))

(re-frame/reg-sub
 :wallet.send/symbol
 :<- [::send-transaction]
 (fn [send-transaction]
   (:symbol send-transaction)))

(re-frame/reg-sub
 :wallet.send/advanced?
 :<- [::send-transaction]
 (fn [send-transaction]
   (:advanced? send-transaction)))

(re-frame/reg-sub
 :wallet.send/camera-flashlight
 :<- [::send-transaction]
 (fn [send-transaction]
   (:camera-flashlight send-transaction)))

(re-frame/reg-sub
 :wallet.send/wrong-password?
 :<- [::send-transaction]
 (fn [send-transaction]
   (:wrong-password? send-transaction)))

(re-frame/reg-sub
 :wallet.send/sign-password-enabled?
 :<- [::send-transaction]
 (fn [{:keys [password]}]
   (and (not (nil? password)) (not= password ""))))

(defn edit-or-transaction-data
  "Set up edit data structure, defaulting to transaction when not available"
  [transaction edit]
  (cond-> edit
    (not (get-in edit [:gas-price :value]))
    (models.wallet/build-edit
     :gas-price
     (money/to-fixed (money/wei-> :gwei (:gas-price transaction))))

    (not (get-in edit [:gas :value]))
    (models.wallet/build-edit
     :gas
     (money/to-fixed (:gas transaction)))))

(re-frame/reg-sub
 :wallet/edit
 :<- [::send-transaction]
 :<- [:wallet]
 (fn [[send-transaction {:keys [edit]}]]
   (edit-or-transaction-data
    send-transaction
    edit)))

(defn- check-sufficient-funds [{:keys [amount symbol] :as transaction} balance]
  (assoc transaction :sufficient-funds?
         (or (nil? amount)
             (money/sufficient-funds? amount (get balance symbol)))))

(defn- check-sufficient-gas [{:keys [amount symbol] :as transaction} balance]
  (assoc transaction :sufficient-gas?
         (or (nil? amount)
             (let [available-ether (get balance :ETH (money/bignumber 0))
                   available-for-gas (if (= :ETH symbol)
                                       (.minus available-ether (money/bignumber amount))
                                       available-ether)]
               (money/sufficient-funds? (-> transaction
                                            :max-fee
                                            money/bignumber
                                            (money/formatted->internal :ETH 18))
                                        (money/bignumber available-for-gas))))))

(def transaction-send-default
  {:method constants/web3-send-transaction
   :symbol :ETH})

(re-frame/reg-sub
 :wallet.send/transaction
 :<- [:balance]
 (fn [balance]
   (-> transaction-send-default
       (models.wallet/transform-data-for-message)
       (models.wallet/add-max-fee)
       (check-sufficient-funds balance)
       (check-sufficient-gas balance))))
