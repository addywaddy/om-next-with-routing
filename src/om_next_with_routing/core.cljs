(ns om-next-with-routing.core
  (:require [secretary.core :as sec :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom])
  (:import goog.History))

(enable-console-print!)

;; STATE
;; =====================================
(defonce app-state {:about {:me "I'm just a poor boy, nobody loves me!" }})

(defmulti read om/dispatch)

(defmethod read :default [_ _ _]
  {:value {}})

(defmethod read :app/about
  [{:keys [state] :as env} key params]
  {:value (get-in @state [:about])})

(def app-parser (om/parser {:read read}))

(def reconciler (om/reconciler {:state app-state :parser app-parser}))

;; HELPERS
;; =====================================
(def navi
  (dom/ul nil
          (dom/li nil
                  (dom/a #js {:href "#/"} "Home"))
          (dom/li nil
                  (dom/a #js {:href "#/about"} "About"))
          (dom/li nil
                  (dom/a #js {:href "#/contact"} "Contact"))))

;; VIEWS
;; =====================================
(defui AboutView
  Object
  (render [this]
          (let [{:keys [me] :as props} (om/props this)]
            (dom/h1 nil me))))

(defui ContactView
  Object
  (render [this]
          (dom/h1 nil "Get in touch!")))

(defui StartView
  Object
  (render [this]
          (dom/h1 nil "Welcome!")))

;; COMPONENT
;; =====================================
(def query-key->view
  {:app/start StartView
   :app/contact ContactView
   :app/about AboutView})

(defn build-component [props]
  (let [component-key (first (keys props))]
    ((om/factory (query-key->view component-key)) (props component-key))))

;; ROOT
;; =====================================
(defui App
  static om/IQuery
  (query [this]
         '[:app/start])
  Object
  (render [this]
          (let [props (om/props this)]
            (dom/div nil
                     navi
                     (build-component props)))))

(om/add-root! reconciler App (gdom/getElement "app"))

;; HISTORY
;; =====================================
(let [history (History.)
      navigation EventType/NAVIGATE]
  (goog.events/listen history
                     navigation
                     #(-> % .-token sec/dispatch!))

  (doto history (.setEnabled true)))

;; ROUTES
;; =====================================
(sec/set-config! :prefix "#")

(defn set-root-query!
  ([query]
   (let [root (om.next/app-root reconciler)]
     (om.next/set-query! root {:query query}))))


(sec/defroute start "/" []
  (set-root-query! '[:app/start]))

(sec/defroute contact "/contact" []
  (set-root-query! '[:app/contact]))

(sec/defroute about "/about" []
  (set-root-query! '[:app/about]))
