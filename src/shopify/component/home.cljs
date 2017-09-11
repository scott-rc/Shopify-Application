(ns shopify.component.home
  (:require [reagent.core :as r]
            [shopify.component.challenge.back-end :refer [back-end-challenge]]
            [shopify.component.challenge.front-end :refer [front-end-challenge]]
            [shopify.component.challenge.data-engineering :refer [data-engineering-challenge]]))

(defn- home-render []
  [:div.ui.grid.container
   [:div.column
    [:h1.ui.centered.header "Welcome Tyrion Lannister!"]

    [:div.ui.three.item.stackable.tabs.menu
     [:a.active.item {:data-tab "back-end"} "Back End Challenge"]
     [:a.item {:data-tab "data-engineering"} "Data Engineering Challenge"]
     [:a.item {:data-tab "front-end"} "Front End Challenge"]]

    [:div.ui.active.tab {:data-tab "back-end"}
     [back-end-challenge]]

    [:div.ui.tab {:data-tab "data-engineering"}
     [data-engineering-challenge]]

    [:div.ui.tab {:data-tab "front-end"}
     [front-end-challenge]]]])

(defn- activate-tabs []
  (-> (js/jQuery ".menu .item")
      (.tab)))

(defn home []
  (r/create-class {:reagent-render home-render
                   :component-did-mount activate-tabs}))
