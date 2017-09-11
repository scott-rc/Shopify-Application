(ns shopify.util.react
  (:require [reagent.core :as r]))

(def css-transition-group
  (r/adapt-react-class js/React.addons.CSSTransitionGroup))
