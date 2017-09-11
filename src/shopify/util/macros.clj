(ns shopify.util.macros)

(defmacro handler-fn
  ([& body]
   `(fn [~'event] ~@body nil)))
