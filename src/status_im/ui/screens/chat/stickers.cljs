(ns status-im.ui.screens.chat.stickers
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.components.react :as react]
            [status-im.ui.components.icons.vector-icons :as vector-icons]
            [re-frame.core :as re-frame]
            [status-im.ui.components.colors :as colors]))

(defview stickers-view []
  [react/view {:style {:background-color :white}}
   [react/view {:style {:height 255 :align-items :center :justify-content :center}}
    [vector-icons/icon :icons/stickers-big {:color colors/gray}]
    [react/text {:style {:margin-top 8 :font-size 17}} "You don’t have any stickers yet"]
    [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :stickers])}
     [react/text {:style {:margin-top 17 :font-size 15 :color colors/blue}} "Get Stickers"]]]
   [react/view {:style {:flex-direction :row :padding-horizontal 12}}
    [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :stickers])}
     [react/view {:style {:background-color colors/blue :height 28 :width 28 :border-radius 14 :align-items :center :justify-content :center :margin-vertical 8}}
      [vector-icons/icon :icons/add {:width 20 :height 20 :color colors/white}]]]
    [react/view {:style {:margin-top 8 :margin-left 22 :align-items :center}}
     [react/view {:style {:background-color colors/gray :height 28 :width 28 :border-radius 14 :align-items :center :justify-content :center}}
      [vector-icons/icon :icons/clock]]
     [react/view {:style {:margin-top 6 :height 2 :width 16 :background-color colors/blue}}]]]])