(ns server.core
  (:require [ring.util.response :as response]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [environ.core :refer [env]])
  (:gen-class))

(defn handler [request]
  (-> (response/resource-response "index.html" {:root "public"})
      (response/content-type "text/html")))

(def dev-app (wrap-reload (wrap-defaults #'handler site-defaults)))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8080))]
    (jetty/run-jetty (wrap-defaults #'handler site-defaults) {:port port :join? false})))
