(ns shopify.component.code
  (:require [reagent.core :as r]))

(defn code-render [slot]
  [:div.ui.left.aligned.code.segment
   [:pre
    [:code.clojure slot]]])

(defn- highlight []
  (.each (js/jQuery "pre code") #(js/hljs.highlightBlock %2)))

(defn code [slot]
  (r/create-class {:reagent-render code-render
                   :component-did-mount highlight}))
