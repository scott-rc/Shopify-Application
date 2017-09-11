(ns shopify.util.json
  (:require [clojure.string :as str]
            [shopify.state :refer [app-state]]))

(defn show-invalid-json []
  ((.-warning js/toastr) "Please fix the JSON in the Input Field" "Invalid JSON!"))

(defn parse [json]
  (-> (js/JSON.parse json)
      (js->clj :keywordize-keys true)))

(defn stringify [map]
  (-> (clj->js map)
      (js/JSON.stringify nil 2)))

(defn stringify-flat [map]
  (-> (clj->js map)
      (js/JSON.stringify)
      (str/replace "}," "},\n")
      (str/replace "{" "    {")
      (str/replace "\":" "\": ")
      (str/replace "[" "[\n")
      (str/replace "]" "\n]")))

(defn stringify-fluid [map]
  (if (:window/tablet? @app-state)
    (stringify map)
    (stringify-flat map)))

(defn format [json]
  (-> (js/JSON.parse json)
      (stringify)))

(defn format-flat [json]
  (-> (js/JSON.parse json)
      (stringify-flat)))

(defn format-fluid [json]
  (-> (js/JSON.parse json)
      (stringify-fluid)))
