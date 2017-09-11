(ns shopify.component.challenge.front-end
  (:require [reagent.core :as r]
            [shopify.util.state :as us]
            [shopify.component.code :refer [code]]
            [shopify.state :refer [app-state]])
  (:require-macros [shopify.util.macros :refer [handler-fn]]))

(def ^:private source "(ns shopify.component.challenge.front-end\r\n  (:require [reagent.core :as r]\r\n            [shopify.state :refer [app-state]]\r\n            [shopify.util.state :as us])\r\n  (:require-macros [shopify.util.macros :refer [handler-fn]]))\r\n\r\n(defn- valid-email? [input]\r\n  (when-not (nil? input)\r\n    (re-matches #\"(?i)[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\" input)))\r\n\r\n(defn- sign-up [input]\r\n  (if (valid-email? input)\r\n    (us/emit! :front-end/update-status\r\n              {:status \"success\" :text \"subscribed successfully\"})\r\n    (us/emit! :front-end/update-status\r\n              {:status \"error\" :text \"please enter a valid email address\"})))\r\n\r\n(def ^:private sign-up-handler\r\n  (handler-fn\r\n   (sign-up (:front-end/input @app-state))\r\n   (us/emit! :front-end/update-show-status? true)))\r\n\r\n(defn- email-address-input []\r\n  [:input {:placeholder \"Email Address\"\r\n           :type \"text\"\r\n           :on-change (handler-fn\r\n                       (us/emit! :front-end/update-input\r\n                                 (.-target.value event)))}])\r\n\r\n(defn- input-tablet []\r\n  [:div.ui.form\r\n   [:div.field\r\n    [:div.ui.large.input\r\n     [email-address-input]]]\r\n   [:div.ui.large.fluid.button\r\n    {:on-click sign-up-handler} \"Sign up now\"]])\r\n\r\n(defn- input-desktop []\r\n  [:div.ui.inline.field\r\n   [:div.ui.big.input\r\n    [email-address-input]]\r\n   [:div.ui.big.button\r\n    {:on-click sign-up-handler} \"Sign up now\"]])\r\n\r\n(defn- front-end-challenge-render []\r\n  (let [show-source? (r/atom false)]\r\n    (fn []\r\n      [:div\r\n       [:h1.ui.center.aligned.challenge.header \"Front End Challenge\"\r\n        [:div\r\n         [:div.ui.fluid.button\r\n          {:on-click (handler-fn\r\n                      (reset! show-source? true))} \"Source\"]\r\n\r\n         [:div.ui.center.aligned.segment.frontend\r\n          [:h1 \"Stay up to date with ecommerce trends with Shopify's newsletter\"]\r\n          [:p \"Subscribe to Shopify's weekly newsletter for free marketing tips\"]\r\n\r\n          (if (:window/tablet? @app-state)\r\n            [input-tablet]\r\n            [input-desktop])\r\n\r\n          (when (:front-end/show-status? @app-state)\r\n            [:div {:class (:status (:front-end/status @app-state))}\r\n             (:text (:front-end/status @app-state))])\r\n\r\n          [:br]\r\n          [:p \"Unsubscribe at any time.\"]]]]])))")

(defn- valid-email? [input]
  (when-not (nil? input)
    (re-matches #"(?i)[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" input)))

(defn- sign-up [input]
  (if (valid-email? input)
    (us/emit! :front-end/update-status
              {:status "success" :text "subscribed successfully"})
    (us/emit! :front-end/update-status
              {:status "error" :text "please enter a valid email address"})))

(def ^:private sign-up-handler
  (handler-fn
   (sign-up (:front-end/input @app-state))
   (us/emit! :front-end/update-show-status? true)))

(defn- email-address-input []
  [:input {:placeholder "Email Address"
           :type "text"
           :on-change (handler-fn
                       (us/emit! :front-end/update-input
                                 (.-target.value event)))}])

(defn- input-tablet []
  [:div.ui.form
   [:div.field
    [:div.ui.large.input
     [email-address-input]]]
   [:div.ui.large.fluid.button
    {:on-click sign-up-handler} "Sign up now"]])

(defn- input-desktop []
  [:div.ui.inline.field
   [:div.ui.big.input
    [email-address-input]]
   [:div.ui.big.button
    {:on-click sign-up-handler} "Sign up now"]])

(defn- front-end-challenge-render []
  (let [show-source? (r/atom false)]
    (fn []
      [:div
       [:h1.ui.center.aligned.challenge.header "Front End Challenge"]

       (if @show-source?
         [:div
          [:div.ui.fluid.button
           {:on-click (handler-fn
                       (reset! show-source? false))} "Challenge"]
          [code source]]

         [:div
          [:div.ui.fluid.button
           {:on-click (handler-fn
                       (reset! show-source? true))} "Source"]

          [:div.ui.center.aligned.segment.frontend
           [:h1 "Stay up to date with ecommerce trends with Shopify's newsletter"]
           [:p "Subscribe to Shopify's weekly newsletter for free marketing tips"]

           (if (:window/tablet? @app-state)
             [input-tablet]
             [input-desktop])

           (when (:front-end/show-status? @app-state)
             [:div {:class (:status (:front-end/status @app-state))}
              (:text (:front-end/status @app-state))])

           [:br]
           [:p "Unsubscribe at any time."]]])])))

(defn front-end-challenge []
  (r/create-class {:reagent-render front-end-challenge-render}))
