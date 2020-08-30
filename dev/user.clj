(ns user
  (:require [integrant.repl :as ig-repl]
            [contacts.system :as system]
            [integrant.core :as ig]))

(ig-repl/set-prep! (fn [] system/system-config))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(comment
  (go)
  (halt)
  (reset)
  (reset-all)
  2
  (map inc [1 2 3 4])
  (ig/ref :contacts.system/handler)
  (ig/ref-key (ig/ref :contacts.system/handler))
  (app {:request-method :post
        :uri            "/api/contacts/"})

  (r/routes router)
  (r/match-by-path router "/api/ping")
  (r/match-by-path router "/swagger.json")
  (r/match-by-path router "/api/users/")
  {:a {:b {:c {:d [1 2 3 4] :e {:a 1 :b 2 :c 3}}}} :b {#{1 2 3} 1}}
  )
