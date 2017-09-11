(ns shopify.component.footer
  (:require [reagent.core :as r]))

(defn footer-render []
  [:footer.ui.center.aligned.basic.segment
   [:a {:href "https://github.com/scott-rc/Shopify-Application"
        :target "_blank"}
    [:i.github.large.icon]
    "GitHub"]])

(defn footer []
  (r/create-class {:reagent-render footer-render}))
