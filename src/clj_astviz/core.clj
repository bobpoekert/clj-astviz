(ns clj-astviz.core
  (:gen-class)
  (require [dorothy.core :as gv]))

(defprotocol ToGraph
  (to-graph [this parent-id]))

(extend-protocol ToGraph
  clojure.lang.Seqable
    (to-graph [ast parent-id]
      (let [nid (str (gensym))]
        (concat
          (let [node [nid {:label (str (type ast))}]]
            (if (= parent-id ::empty)
              [node]
              [[parent-id nid] node]))
          (mapcat
            (fn [subgraph]
              (let [res (to-graph subgraph nid)]
                (if (= parent-id ::empty)
                  res
                  (cons [parent-id nid] res))))
            ast))))
  Object
    (to-graph [ast parent-id]
      (let [nid (str (gensym))]
          [[nid {:label (str (type ast) ":  " ast)}]
           [parent-id nid]])))

(defn ast-to-dot
  [ast]
  (gv/dot
    (gv/digraph
      (filter #(not (= % ::empty))
        (sort-by #(not (map? (second %)))
          (into #{} (to-graph ast ::empty)))))))

(defn save-graph!
  [fname ast]
  (gv/save!
    (ast-to-dot ast)
    fname
    {:format "png"}))

(defn -main
  [target-name code]
  (do
    (save-graph! target-name (read-string code))
    (System/exit 0)))
