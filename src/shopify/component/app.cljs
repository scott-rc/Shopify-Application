(ns shopify.component.app
  (:require [shopify.component.header :refer [header]]
            [shopify.component.footer :refer [footer]]
            [shopify.state :refer [app-state]]
            [shopify.util.react :as ur]
            [reagent.core :as r]))

(defn- get-current-page []
  (fn []
    [:div
     [ur/css-transition-group
      {:transition-name "fade" :transition-leave false}
      ^{:key (:core/current-path @app-state)}
      [(:core/current-page @app-state)]]]))

(defn- app-render []
  [:div
   [header]
   [get-current-page]
   [footer]])

(defn app []
  (r/create-class {:reagent-render app-render}))
