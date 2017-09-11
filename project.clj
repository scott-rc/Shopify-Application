(defproject shopify "0.1"
  :description "Scott C's shopify application!"
  :url "scotts-shopify-application.herokuapp.com"
  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.854"]
                 [org.clojure/core.async  "0.3.443" :exclusions [org.clojure/tools.reader]]
                 [reagent "0.5.1" :exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "0.13.3-0"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.2.0"]
                 [ring "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [environ "1.1.0"]
                 [proto-repl "0.3.1"]]

  :plugins [[lein-heroku "0.5.3"]
            [lein-cljfmt "0.5.7"]
            [lein-figwheel "0.5.12"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :uberjar-name "scotts-uber-shopify-app.jar"

  :heroku {:app-name "scotts-shopify-application"
           :jdk-version "1.8"
           :include-files ["target/scotts-uber-shopify-app.jar"]
           :process-types { "web" "java -jar target/scotts-uber-shopify-app.jar"}}

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/shopify"]
                :figwheel {:open-urls ["http://localhost:3449/"]}
                :compiler {:main shopify.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/shopify.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}

               {:id "min"
                :source-paths ["src/shopify"]
                :compiler {:main shopify.core
                           :output-to "resources/public/js/compiled/shopify.js"
                           :optimizations :advanced
                           :infer-externs true
                           :pretty-print false}}]}

  :prep-tasks [["cljfmt" "fix"]
               ["compile"]
               ["cljsbuild" "once" "min"]]

  :figwheel {:ring-handler server.core/dev-app
             :css-dirs ["resources/public/css"]}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.4"]
                                  [figwheel-sidecar "0.5.12"]
                                  [com.cemerick/piggieback "0.2.2"]]
                   :source-paths ["src" "dev"]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}

             :uberjar {:main server.core
                       :aot :all}})
