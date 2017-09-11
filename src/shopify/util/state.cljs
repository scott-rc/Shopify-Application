(ns shopify.util.state
  (:require [cljs.core.async :refer [put!]]
            [shopify.util.atom :as ua]))

(defn emit!
  ([e] (put! shopify.state/event-channel [e]))
  ([e v] (put! shopify.state/event-channel [e v])))

(defn update-event [k]
  (fn [v]
    (ua/assoc!! shopify.state/app-state k v)))
