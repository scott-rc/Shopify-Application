(ns shopify.state
  (:require [reagent.core :as r]
            [cljs.core.async :refer [chan <!]]
            [shopify.util.default :as default]
            [shopify.util.state :as us]
            [shopify.util.atom :as ua])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defonce event-channel (chan))

(defonce app-state
  (r/atom
   {:core/current-path nil
    :core/current-page  nil

    :window/mobile?  nil
    :window/tablet? nil
    :window/laptop? nil

    :front-end/input nil
    :front-end/status nil
    :front-end/show-status? false

    :data-engineering/table1 (:table1 default/data-engineering)
    :data-engineering/table2 (:table2 default/data-engineering)
    :data-engineering/table1-key (:table1-key default/data-engineering)
    :data-engineering/table2-key (:table2-key default/data-engineering)
    :data-engineering/table1-name (:table1-name default/data-engineering)
    :data-engineering/table2-name (:table2-name default/data-engineering)
    :data-engineering/join-type (:join-type default/data-engineering)
    :data-engineering/result (:result default/data-engineering)

    :back-end/json (:json default/back-end)
    :back-end/result (:result default/back-end)}))

(defonce events
  {:window/update-width (fn [width]
                          (ua/assoc!! app-state :window/mobile? (< width 425))
                          (ua/assoc!! app-state :window/tablet? (< width 768))
                          (ua/assoc!! app-state :window/laptop? (< width 1024)))

   :front-end/update-input (us/update-event :front-end/input)
   :front-end/update-status (us/update-event :front-end/status)
   :front-end/update-show-status? (us/update-event :front-end/show-status?)

   :data-engineering/update-table1 (us/update-event :data-engineering/table1)
   :data-engineering/update-table2 (us/update-event :data-engineering/table2)
   :data-engineering/update-table1-key (us/update-event :data-engineering/table1-key)
   :data-engineering/update-table2-key (us/update-event :data-engineering/table2-key)
   :data-engineering/update-table1-name (us/update-event :data-engineering/table1-name)
   :data-engineering/update-table2-name (us/update-event :data-engineering/table2-name)
   :data-engineering/update-join-type (us/update-event :data-engineering/join-type)
   :data-engineering/update-result (us/update-event :data-engineering/result)
   :data-engineering/reset (fn []
                             (let [{:keys [table1 table2 table1-key table2-key
                                           table1-name table2-name join-type]} default/data-engineering]
                               (ua/assoc!! app-state
                                           :data-engineering/table1 table1
                                           :data-engineering/table2 table2
                                           :data-engineering/table1-key table1-key
                                           :data-engineering/table2-key table2-key
                                           :data-engineering/table1-name table1-name
                                           :data-engineering/table2-name table2-name
                                           :data-engineering/join-type join-type
                                           :data-engineering/result nil)))

   :back-end/update-json (us/update-event :back-end/json)
   :back-end/update-result (us/update-event :back-end/result)
   :back-end/reset (fn []
                     (let [{:keys [json result]} default/back-end]
                       (ua/assoc!! app-state :back-end/json json :back-end/result result)))})

(go
  (while true
    (let [[k v] (<! event-channel)]
      ((k events) v))))
