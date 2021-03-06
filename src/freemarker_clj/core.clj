(ns freemarker-clj.core
  (:use [freemarker-clj.shim :only [map->model]])
  (:import [freemarker.template
            Configuration
            DefaultObjectWrapper
            TemplateMethodModel]))


(defn gen-config
  [template-home & {:keys [shared] :or {shared {}}}]
  (let [cfg (doto (Configuration.)
              (.setDirectoryForTemplateLoading (java.io.File. template-home))
              (.setObjectWrapper (DefaultObjectWrapper.)))]
    (doseq [[k v] (map->model shared)]
      (.setSharedVariable cfg k v))
    cfg))


(defn render
  [cfg path model & {:keys [map->model?] :or {map->model? true}}]
  (with-out-str
    (.process (.getTemplate cfg path)
              (if map->model?
                (map->model model)
                model)
              *out*)))
