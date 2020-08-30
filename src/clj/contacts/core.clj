(ns contacts.core
  (:require [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring :as ring]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                                     format-request-middleware
                                                     format-response-middleware]]
            [reitit.ring.coercion :refer [coerce-request-middleware
                                          coerce-response-middleware]]
            [reitit.coercion.spec]
            [muuntaja.core :as m]
            [contacts.routes :as contacts.routes]
            [reitit.core :as r]
            [contacts.middleware :as mw]))

(def default-handler
  (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/"})
    (ring/redirect-trailing-slash-handler)
    (ring/create-default-handler {:not-found (constantly {:status 404
                                                          :body   "Route not found"})})))
(defn create-app [db]
  (ring/ring-handler (ring/router
                       [["/swagger.json" {:get {:no-doc  true
                                                :swagger {:info {:tittle "Contact Book API"}}
                                                :handler (swagger/create-swagger-handler)}}]
                        ["/api"
                         contacts.routes/ping
                         contacts.routes/contacts]]
                       {:data {:db         db
                               :coercion   reitit.coercion.spec/coercion
                               :muuntaja   m/instance
                               :middleware [format-negotiate-middleware
                                            format-response-middleware
                                            exception-middleware
                                            format-request-middleware
                                            coerce-request-middleware
                                            coerce-response-middleware
                                            mw/db]}})
                     default-handler))

