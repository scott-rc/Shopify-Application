(ns shopify.util.window
  (:require [shopify.util.state :as us]))

(defn set-window-width []
  (us/emit! :window/update-width (or js/window.innerWidth
                                     js/document.documentElement.clientWidth
                                     js/document.body.clientWidth)))
