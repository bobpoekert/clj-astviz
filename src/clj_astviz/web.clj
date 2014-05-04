(ns clj-astviz.web
  (:gen-class)
  (require [clj-astviz.core :as c]
           [compojure.route :as route]
           [compojure.core :as compojure]
           [clojure.java.io :as io]
           [ring.adapter.jetty :as jetty])
  (import [java.io ByteArrayInputStream]))

(def index (slurp (io/resource "index.html")))

(compojure/defroutes app
  (compojure/GET "/" []
    {:body index})
  (compojure/GET "/png/:in" [in]
    (binding [*read-eval* false]
      (let [res (c/graph-bytes (read-string in))]
        {:status 200
         :headers {"Content-Type" "image/png"}
         :body (ByteArrayInputStream. res)}))))

(defn -main
  [& args]
  (jetty/run-jetty
    app {:port 5001}))
