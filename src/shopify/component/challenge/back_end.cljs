(ns shopify.component.challenge.back-end
  (:require [reagent.core :as r]
            [shopify.util.json :as uj]
            [shopify.state :refer [app-state]]
            [shopify.component.code :refer [code]]
            [shopify.util.atom :as ua]
            [shopify.util.state :as us])
  (:require-macros [shopify.util.macros :refer [handler-fn]]))

(def ^:private source "(ns shopify.component.challenge.back-end\r\n  (:require [shopify.util.json :as uj]\r\n            [shopify.util.atom :as ua]))\r\n\r\n(defn- invalid-length? [val-length {:keys [min max]}]\r\n  (or\r\n   (and (some? min) (< val-length min))\r\n   (and (some? max) (> val-length max))))\r\n\r\n(defn- invalid? [val {:keys [required length type]}]\r\n  (or\r\n   (and (true? required) (nil? val))\r\n   (when (some? val)\r\n     (or\r\n      (and (some? type) (js* \"typeof ~{} !== ~{}\" val type))\r\n      (and (some? length) (invalid-length? (count val) length))))))\r\n\r\n(defn- get-invalid-customers [customers fields validations]\r\n  (let [invalid-customers (atom [])]\r\n    (doseq [customer customers]\r\n      (let [invalid-fields (atom [])]\r\n        (doseq [field fields]\r\n          (when (invalid? (field customer) (field validations))\r\n            (ua/conj!! invalid-fields (name field))))\r\n        (when-not (empty? @invalid-fields)\r\n          (ua/conj!! invalid-customers {:id (:id customer)\r\n                                        :invalid_fields @invalid-fields}))))\r\n    @invalid-customers))")

(defn- invalid-length? [val-length {:keys [min max]}]
  (or
   (and (some? min) (< val-length min))
   (and (some? max) (> val-length max))))

(defn- invalid? [val {:keys [required length type]}]
  (or
   (and (true? required) (nil? val))
   (when (some? val)
     (or
      (and (some? type) (js* "typeof ~{} !== ~{}" val type))
      (and (some? length) (invalid-length? (count val) length))))))

(defn- get-invalid-customers [customers fields validations]
  (let [invalid-customers (atom [])]
    (doseq [customer customers]
      (let [invalid-fields (atom [])]
        (doseq [field fields]
          (when (invalid? (field customer) (field validations))
            (ua/conj!! invalid-fields (name field))))
        (when-not (empty? @invalid-fields)
          (ua/conj!! invalid-customers {:id (:id customer)
                                        :invalid_fields @invalid-fields}))))
    @invalid-customers))

(defn- validate! [json]
  (let [data (uj/parse json)
        customers (:customers data)
        validations (into (hash-map) (:validations data))
        fields (keys validations)
        invalid-customers (get-invalid-customers customers fields validations)]
    (uj/stringify {:invalid_customers invalid-customers})))

(defn- back-end-challenge-render []
  (let [show-source? (r/atom false)]
    (fn []
      [:div.ui.centered.grid
       [:div.row
        [:h1.ui.header "Back End Challenge"]]

       [:div.row
        (if @show-source?

          [:div.ui.basic.padded.segment.container
           [:div.ui.fluid.button
            {:on-click (handler-fn
                        (reset! show-source? false))} "Challenge"]
           [code source]]

          [:div.ui.grid.container
           [:div.four.ui.buttons
            [:div.ui.button
             {:on-click (handler-fn
                         (try
                           (us/emit! :back-end/update-json
                                     (uj/format (:back-end/json @app-state)))
                           (catch js/Object e (uj/show-invalid-json))))} "Format"]
            [:div.ui.button
             {:on-click (handler-fn
                         (try
                           (us/emit! :back-end/update-result
                                     (validate! (:back-end/json @app-state)))
                           (catch js/Object e (uj/show-invalid-json))))} "Validate"]
            [:div.ui.button
             {:on-click (handler-fn
                         (us/emit! :back-end/reset))} "Reset"]
            [:div.ui.button
             {:on-click (handler-fn
                         (reset! show-source? true))} "Source"]]

           [:div.centered.row
            [:div.column
             [:div.ui.stackable.two.column.mobile.reversed.grid
              [:div.column
               [:div.ui.form
                [:div.field
                 [:textarea {:rows 30
                             :value (:back-end/json @app-state)
                             :on-change (handler-fn
                                         (us/emit! :back-end/update-json
                                                   (.-target.value event)))}]]]]
              [:div.column
               [:div.ui.segment
                [:pre (:back-end/result @app-state)]]]]]]

           [:div.centered.row
            [:div.ui.horizontal.bulleted.list
             (for [n (range 1 5)]
               [:a.item {:key n
                         :target "_blank"
                         :href (str "https://backend-challenge-winter-2017.herokuapp.com/customers.json?page=" n)}
                (str "Customers Page " n)])]]])]])))

(defn back-end-challenge []
  (r/create-class {:reagent-render back-end-challenge-render}))
