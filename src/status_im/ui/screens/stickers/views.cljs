(ns status-im.ui.screens.stickers.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [status-im.i18n :as i18n]
            [status-im.ui.components.react :as react]
            [status-im.ui.screens.stickers.styles :as styles]
            [status-im.ui.components.status-bar.view :as status-bar]
            [status-im.ui.components.styles :as components.styles]
            [status-im.ui.components.toolbar.view :as toolbar]
            [status-im.ui.components.colors :as colors]
            [status-im.ui.components.icons.vector-icons :as icons]))

(def sticker-packs-data [{:id "id1"
                          :name "Sticker pack 1"
                          :uri ""
                          :author "Andrey Shovkoplyas"
                          :icon "hash" ;; to show in the packs list etc
                          :preview "hash"
                          :price 0 ;; 0 means free
                          :stickers [{:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""}]
                          :debug {:color :orange}}
                         {:id "id2"
                          :name "Sticker pack 2"
                          :uri ""
                          :author "Andrey Shovkoplyas"
                          :icon "hash" ;; to show in the packs list etc
                          :preview "hash"
                          :price 1000 ;; 0 means free
                          :stickers [{:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""}]
                          :debug {:color :blue}}
                         {:id "id3"
                          :name "Sticker pack 3"
                          :uri ""
                          :author "Salvador Dalí"
                          :icon "hash" ;; to show in the packs list etc
                          :preview "hash"
                          :price 10000 ;; 0 means free
                          :stickers [{:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""}]
                          :debug {:color :red :not-enough-snt? true}}
                         {:id "id4"
                          :name "Sticker pack 4"
                          :uri ""
                          :author "Salvador Dalí"
                          :icon "hash" ;; to show in the packs list etc
                          :preview "hash"
                          :price 2500 ;; 0 means free
                          :stickers [{:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""} {:uri ""}]
                          :debug {:color :yellow :no-snt? true}}])

(defview pack-bage [{:keys [name author price debug]}]
  (let [{:keys [color no-snt? not-enough-snt?]} debug]
    [react/view {:margin-bottom 27}
     [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :stickers-pack])}
      [react/view {:border-radius 8 :background-color color :height 200}]]
     [react/view {:height 64 :align-items :center :flex-direction :row}
      [react/view {:height 40 :width 40 :background-color color :border-radius 20}]
      [react/view {:padding-horizontal 16 :flex 1}
       [react/text {:style {:font-size 15}} name]
       [react/text {:style {:font-size 15 :color colors/gray :margin-top 6}} author]]
      [react/view {:background-color (if not-enough-snt? colors/gray colors/blue)
                   :border-radius 14 :flex-direction :row :padding-horizontal 8 :height 28 :align-items :center}
       [icons/icon :icons/logo {:color colors/white :width 12 :height 12}]
       [react/text {:style {:margin-left 8 :font-size 15 :color colors/white}}
        (cond no-snt?
              "Buy with SNT"
              (zero? price)
              "Free"
              :else
              price)]]]]))

(defview stickers []
  [react/view styles/screen
   [status-bar/status-bar]
   [react/keyboard-avoiding-view components.styles/flex
    [toolbar/simple-toolbar "Sticker market"]
    [react/view {:style {:padding-horizontal 16 :padding-top 8 :flex 1}}
     ;[react/view {:style {:height 36 :border-radius 8 :background-color colors/gray-lighter}}]
     [react/scroll-view {:keyboard-should-persist-taps :handled :style {:margin-top 16 :flex 1}}
      [react/view
       (for [pack sticker-packs-data]
         ^{:key pack}
         [pack-bage pack])]]]]])
(defn sticker []
  [react/view {:height 60 :width 60 :margin 16 :background-color :blue :border-radius 32}])

(defview pack []
  [react/view styles/screen
   [status-bar/status-bar]
   [react/keyboard-avoiding-view components.styles/flex
    [toolbar/simple-toolbar]
    [react/view {:height 94 :align-items :center :flex-direction :row :padding-horizontal 16}
     [react/view {:height 64 :width 64 :background-color :orange :border-radius 32}]
     [react/view {:padding-horizontal 16 :flex 1}
      [react/text {:style {:font-size 22 :font-weight :bold}} "Name"]
      [react/text {:style {:font-size 15 :color colors/gray :margin-top 6}} "Author"]]
     [react/view {:background-color colors/blue :border-radius 14 :flex-direction :row :padding-horizontal 8 :height 28 :align-items :center}
      [icons/icon :icons/logo {:color colors/white :width 12 :height 12}]
      [react/text {:style {:margin-left 8 :font-size 15 :color colors/white}} "1000"]]]
    [react/view {:style {:padding-top 8 :flex 1}}
     [react/scroll-view {:keyboard-should-persist-taps :handled :style {:flex 1}}
      [react/view {:flex-direction :row :flex-wrap :wrap}
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]
       [sticker]]]]]])