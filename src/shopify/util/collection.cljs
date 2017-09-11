(ns shopify.util.collection)

(defn in? [coll el]
  (some #(= el %) coll))

