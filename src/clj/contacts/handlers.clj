(ns contacts.handlers
  (:require [contacts.db :as contacts-db]))

(defn get-all [parameters]
  (contacts-db/get-all parameters))

(defn get-by-id [parameters]
  (contacts-db/get-by-id parameters))

(defn create [parameters]
  (contacts-db/create parameters))

(defn delete [parameters]
  (contacts-db/delete parameters))

(defn update [parameters]
  (contacts-db/update parameters))
