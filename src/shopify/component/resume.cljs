(ns shopify.component.resume
  (:require [shopify.util.atom :as ua]
            [reagent.core :as r]))

(defn- create-horizontal-list [list]
  (let [i (atom 0)]
    [:div.ui.horizontal.bulleted.list
     (for [item list]
       [:div.item {:key (ua/inc!! i)} item])]))

(defn- create-list [list]
  (let [i (atom 0)]
    [:div.ui.bulleted.list
     (for [item list]
       (if (coll? item)
         [:div.item {:key (ua/inc!! i)}
          [:div {:key (ua/inc!! i)} (first item)]
          (create-list (rest item))]
         [:div.item {:key (ua/inc!! i)} item]))]))

(defn- resume-render []
  [:div.ui.grid.centered.stackable.container
   [:div.two.column.row

    [:div.four.wide.column
     [:div.ui.card
      [:div.content
       [:a.header "Scott Cote"]
       [:div.description
        [:p "1071 Ambleside Dr. #808"]
        [:p "Ottawa ON K2B 6V4"]
        [:p "613-298-0281"]
        [:p "scrcote@gmail.com"]]]]

     [:h3.ui.header "Personality Traits"]
     (create-list ["Active Listener"
                   "Accepts Feedback"
                   "Adaptable"
                   "Attentive"
                   "Charismatic"
                   "Communicative"
                   "Reliable"
                   "Team Player"])

     [:h3.ui.header "Experience"]
     [:div.ui.bulleted.list
      [:div.item
       [:h4.ui.header "Languages"]
       (create-list ["C#"
                     ["CSS" "SCSS"]
                     ["HTML" "Pug" "Hiccup"]
                     ["Java" "Kotlin" "Clojure"]
                     ["JavaScript" "ES6 / Babel" "CoffeeScript" "TypeScript"]
                     ["SQL" "Postgres" "MySQL" "Oracle 11g"]])]

      [:div.item
       [:h4.ui.header "Software"]
       (create-list ["Atom"
                     "Eclipse"
                     "Emacs"
                     "IntelliJ IDEA"
                     "MySQL Workbench"
                     "Oracle SQL Developer"
                     "Visual Studio"
                     "Visual Studio Code"])]]]

    [:div.column
     [:h3.ui.header "Objective"]

     [:p "Student seeking experience to advance career as a computer programmer."]

     [:h3.ui.header "Education"]
     [:p "Computer Programmer - 2015 - Present"]
     [:p "Algonquin College School of Advanced Technology"]
     [:p "Expected Completion: December 2017"]

     [:h3.ui.header "Employment"]

     [:h4.ui.header "Programmer, Employment & Social Development Canada"]
     (create-horizontal-list ["Gatineau, QC" "January 2017 - Present"])
     (create-list ["Participated in the development of the client’s application by utilizing .NET MVC, C#, JavaScript, jQuery, HTML and SQL programming/scripting languages in order to resolve issues and develop new features"
                   "Worked with the architect, project managers, and other application teams to resolve issues and ensure the overall solution meets project requirements"
                   "Maintained the existing application by making modifications as required"
                   "Supported the development and testing of the entire stack of the application"
                   "Actively contributed in team meetings"
                   "Performed other assigned duties as required"])

     [:h4.ui.header "Home Meal Replacement Clerk, Independent Grocer"]
     (create-horizontal-list ["Orleans, ON" "2016 -2017"])
     (create-list ["Provided exceptional customer service"
                   "Executed company-directed promotions and programs"
                   "Prepared and cooked a variety of products"
                   "Maintained a clean and sanitized work area"
                   "Completed accurate sales transactions"])

     [:h4.ui.header "Back Line & Receiver, Arby's"]
     (create-horizontal-list ["Orleans, ON" "2015 - 2016"])
     (create-list ["Operated and cleaned a variety of kitchen appliances"
                   "Maintained food safety and sanitation standards in kitchen"
                   "Prepared food items according to customer requests"
                   ["Receiver Responsibilities:"
                    "Ensured order accuracy"
                    "Replenished stock in a timely fashion"
                    "Rotated merchandise"
                    "Labeled and dated all appropriate items"
                    "Assisted management with inventory control"]])]]])

(defn resume []
  (r/create-class {:reagent-render resume-render}))
