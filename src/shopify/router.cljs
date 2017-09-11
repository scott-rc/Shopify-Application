(ns shopify.router
  (:require [accountant.core :as a]
            [secretary.core :as s]
            [shopify.state :refer [app-state]]
            [shopify.util.atom :as ua]
            [shopify.component.home :refer [home]]
            [shopify.component.resume :refer [resume]])
  (:require-macros [secretary.core :refer [defroute]]))

(def ^:private routes [[#'home ""]
                       [#'resume]])

(defn- setup-routes []
  (doseq [[comp path] routes]
    (defroute
      (str "/" (or path (:name (meta comp))))
      []
      (ua/assoc!! app-state :core/current-page comp))))

(defn- setup-navigation []
  (a/configure-navigation!
   {:nav-handler  (fn [path]
                    (s/dispatch! path)
                    (ua/assoc!! app-state :core/current-path path))

    :path-exists? (fn [path]
                    (s/locate-route path))}))

(defn init []
  (setup-routes)
  (setup-navigation)
  (a/dispatch-current!))
