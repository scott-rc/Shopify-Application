(ns shopify.component.challenge.data-engineering
  (:require [clojure.string :refer [blank?]]
            [reagent.core :as r]
            [shopify.util.atom :as ua]
            [shopify.util.json :as uj]
            [shopify.component.code :refer [code]]
            [shopify.util.collection :as uc]
            [shopify.state :refer [app-state]]
            [shopify.util.state :as us])
  (:require-macros [shopify.util.macros :refer [handler-fn]]))

(def ^:private source "(ns shopify.component.challenge.data-engineering\r\n  (:require [clojure.string :refer [blank?]]\r\n            [shopify.util.atom :as ua]\r\n            [shopify.util.collection :as uc]\r\n            [shopify.util.json :as uj]))\r\n\r\n(defn- join [table1 table2 table1-key table2-key side-join?]\r\n  (mapcat (fn [table1-row]\r\n            (as-> (filter #(= (table2-key %)\r\n                              (table1-key table1-row)) table2) filtered\r\n              (if (and side-join? (empty? filtered))\r\n                [table1-row]\r\n                (map #(merge % table1-row) filtered)))) table1))\r\n\r\n(defn- outer-join [table1 table2 table1-key table2-key]\r\n  (let [left-join (join table1 table2 table1-key table2-key true)\r\n        matched-ids (mapv #(table2-key %) left-join)\r\n        leftovers (atom [])]\r\n    (doseq [row table2]\r\n      (when-not (in? matched-ids (table2-key row))\r\n        (ua/conj!! leftovers row)))\r\n    (flatten (conj left-join @leftovers))))\r\n\r\n(def ^:private join-types {:inner-join #(join %1 %2 %3 %4 false)\r\n                           :left-join #(join %1 %2 %3 %4 true)\r\n                           :right-join #(join %2 %1 %4 %3 true)\r\n                           :outer-join #(outer-join %1 %2 %3 %4)})\r\n\r\n(defn- join! [json-table1 json-table2 table1-key table2-key join-type]\r\n  (let [table1 (uj/parse json-table1)\r\n        table2 (uj/parse json-table2)\r\n        result ((join-type join-types) table1 table2 table1-key table2-key)]\r\n    (uj/stringify-fluid result)))")

(defn- join [table1 table2 table1-key table2-key side-join?]
  (mapcat (fn [table1-row]
            (as-> (filter #(= (table2-key %)
                              (table1-key table1-row)) table2) filtered
              (if (and side-join? (empty? filtered))
                [table1-row]
                (map #(merge % table1-row) filtered)))) table1))

(defn- outer-join [table1 table2 table1-key table2-key]
  (let [left-join (join table1 table2 table1-key table2-key true)
        matched-ids (mapv #(table2-key %) left-join)
        leftovers (atom [])]
    (doseq [row table2]
      (when-not (uc/in? matched-ids (table2-key row))
        (ua/conj!! leftovers row)))
    (flatten (conj left-join @leftovers))))

(def ^:private join-types {:inner-join #(join %1 %2 %3 %4 false)
                           :left-join #(join %1 %2 %3 %4 true)
                           :right-join #(join %2 %1 %4 %3 true)
                           :outer-join #(outer-join %1 %2 %3 %4)})

(defn- join! [json-table1 json-table2 table1-key table2-key join-type]
  (let [table1 (uj/parse json-table1)
        table2 (uj/parse json-table2)
        result ((join-type join-types) table1 table2 table1-key table2-key)]
    (uj/stringify-fluid result)))

(defn- join-select []
  [:select.ui.compact.dropdown {:value (:data-engineering/join-type @app-state)
                                :on-change (handler-fn
                                            (us/emit! :data-engineering/update-join-type
                                                      (-> event .-target.value keyword)))}
   [:option {:value "inner-join"} "inner join"]
   [:option {:value "left-join"} "left join"]
   [:option {:value "right-join"} "right join"]
   [:option {:value "outer-join"} "outer join"]])

(defn- table1-key-input []
  [:input {:type "text"
           :placeholder (str (:data-engineering/table1-name @app-state) " key")
           :value (:data-engineering/table1-key @app-state)
           :on-change (handler-fn
                       (us/emit! :data-engineering/update-table1-key
                                 (-> event .-target.value keyword)))}])

(defn- table2-key-input []
  [:input {:type "text"
           :placeholder (str (:data-engineering/table2-name @app-state) " key")
           :value (:data-engineering/table2-key @app-state)
           :on-change (handler-fn
                       (us/emit! :data-engineering/update-table2-key
                                 (-> event .-target.value keyword)))}])

(defn- buttons [show-source?]
  [:div.four.ui.buttons
   [:div.ui.button
    {:on-click (handler-fn
                (try
                  (us/emit! :data-engineering/update-table1
                            (uj/format-fluid (:data-engineering/table1 @app-state)))
                  (us/emit! :data-engineering/update-table2
                            (uj/format-fluid (:data-engineering/table2 @app-state)))
                  (catch js/Object e
                    (uj/show-invalid-json))))} "Format"]

   [:div.ui.button
    {:on-click (handler-fn
                (try
                  (us/emit! :data-engineering/update-result
                            (join!
                             (:data-engineering/table1 @app-state)
                             (:data-engineering/table2 @app-state)
                             (:data-engineering/table1-key @app-state)
                             (:data-engineering/table2-key @app-state)
                             (:data-engineering/join-type @app-state)))
                  (catch js/Object e
                    (uj/show-invalid-json))))} "Join"]

   [:div.ui.button
    {:on-click (handler-fn
                (us/emit! :data-engineering/reset))} "Reset"]

   [:div.ui.button
    {:on-click (handler-fn
                (reset! show-source? true))} "Source"]])

(defn- sql-tablet []
  [:div.left.aligned.row
   [:div.column
    [:div.ui.form
     [:div.field
      [:label (str (:data-engineering/table1-name @app-state) " key")]
      [table1-key-input]]
     [:div.field
      [:label (str (:data-engineering/table2-name @app-state) " key")]
      [table2-key-input]]
     [:div.field
      [:label "join type"]
      [join-select]]]]])

(defn- sql-laptop []
  [:div.centered.row
   [:div.ui.form
    [:div.inline.field
     [:span "select * from " (:data-engineering/table1-name @app-state)]
     [join-select]
     [:span.padded (:data-engineering/table2-name @app-state) " on"]
     [table1-key-input]
     [:span.padded "="]
     [table2-key-input]]]])

(defn- data-engineering-challenge-render []
  (let [show-source? (r/atom false)]
    (fn []
      [:div.ui.centered.grid
       [:div.row
        [:h1.ui.challenge.header "Data Engineering Challenge"]]

       [:div.row
        (if @show-source?
          [:div.ui.basic.padded.segment.container
           [:div.ui.fluid.button {:on-click (handler-fn
                                             (reset! show-source? false))} "Challenge"]
           [code source]]
          [:div.ui.grid.container
           [buttons show-source?]

           (if (:window/laptop? @app-state)
             [sql-tablet]
             [sql-laptop])

           [:div.centered.row
            [:div.column
             [:div.ui.stackable.two.column.grid
              [:div.column
               [:div.ui.form
                [:div.field
                 [:input {:type "text"
                          :value (:data-engineering/table1-name @app-state)
                          :on-change (handler-fn
                                      (us/emit! :data-engineering/update-table1-name
                                                (.-target.value event)))}]]
                [:div.field
                 [:textarea {:rows 9
                             :value (:data-engineering/table1 @app-state)
                             :on-change (handler-fn
                                         (us/emit! :data-engineering/update-table1
                                                   (.-target.value event)))}]]]]
              [:div.column
               [:div.ui.form
                [:div.field
                 [:input {:type "text"
                          :value (:data-engineering/table2-name @app-state)
                          :on-change (handler-fn
                                      (us/emit! :data-engineering/update-table2-name
                                                (.-target.value event)))}]]
                [:div.field
                 [:textarea {:rows 9
                             :value (:data-engineering/table2 @app-state)
                             :on-change (handler-fn
                                         (us/emit! :data-engineering/update-table2
                                                   (.-target.value event)))}]]]]]]]

           (when-not (blank? (:data-engineering/result @app-state))
             [:div.centered.row
              [:div.column
               [:div.ui.segment
                [:pre (:data-engineering/result @app-state)]]]])])]])))

(defn- activate-dropdowns []
  (-> (js/jQuery ".ui.dropdown")
      (.dropdown)))

(defn data-engineering-challenge []
  (r/create-class {:component-did-mount activate-dropdowns
                   :reagent-render data-engineering-challenge-render}))
