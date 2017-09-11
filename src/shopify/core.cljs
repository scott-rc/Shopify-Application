(ns shopify.core
  (:require [reagent.core :as r]
            [shopify.router :as router]
            [shopify.component.app :refer [app]]
            [shopify.util.window :as uw]))

(enable-console-print!)

(uw/set-window-width)

(js/window.addEventListener "resize" uw/set-window-width)

(router/init)

(r/render [app] (js/document.getElementById "app"))
