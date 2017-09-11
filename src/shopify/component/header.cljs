(ns shopify.component.header
  (:require [reagent.core :as r]
            [shopify.state :refer [app-state]]))

(defn- create-header-item [label icon path]
  [:a.item {:href path
            :class (when (= (:core/current-path @app-state) path) "active")}
   [:i {:class (str icon " icon")}] label])

(defn- header-render []
  [:div.ui.secondary.two.item.pointing.menu
   (create-header-item "Home" "home" "/")
   (create-header-item "Resume" "file text" "/resume")])

(defn header []
  (r/create-class {:reagent-render header-render}))
