(ns contacts.handlers
  (:require [contacts.db :as contacts-db]))

(defn get-all [parameters]
  (contacts-db/get-all parameters))

(defn get-by-id [parameters]
  (contacts-db/get-by-id parameters))

(defn create [req]
  (contacts-db/create req))
