(ns specific.sample
  (:require [clojure.spec.alpha]
            [clojure.java.shell :as shell]
            [clojure.string :as string]))

(defn greet [pre sufs]
  (string/join ", " (cons pre sufs)))

(defn cowsay [msg]
  (shell/sh "cowsay" msg)) ; Fails in some environments

(defn some-fun [greeting & names]
  (:out (cowsay (greet greeting names))))

(clojure.spec.alpha/def ::exit (clojure.spec.alpha/and integer? #(>= % 0) #(< % 256)))
(clojure.spec.alpha/def ::out string?)
(clojure.spec.alpha/def ::fun-greeting string?)
(clojure.spec.alpha/fdef greet :ret ::fun-greeting)
(clojure.spec.alpha/fdef cowsay
                         :args (clojure.spec.alpha/cat :fun-greeting ::fun-greeting)
                         :ret (clojure.spec.alpha/keys :req-un [::out ::exit]))
(clojure.spec.alpha/fdef some-fun
                         :args (clojure.spec.alpha/cat :greeting ::fun-greeting
                                                       :names (clojure.spec.alpha/* string?))
                         :ret string?)
