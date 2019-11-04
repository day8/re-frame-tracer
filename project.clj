(defproject day8/re-frame-tracer "see :git-version below https://github.com/arrdem/lein-git-version"
  :description "Tracer for clairvoyant that is optimised for cljs-devtools"
  :url "http://github.com/day8/re-frame-tracer"
  :license {:name "MIT"}

  :git-version
  {:status-to-version
   (fn [{:keys [tag version branch ahead ahead? dirty?] :as git}]
     (assert (re-find #"\d+\.\d+\.\d+" tag)
       "Tag is assumed to be a raw SemVer version")
     (if (and tag (not ahead?) (not dirty?))
       tag
       (let [[_ prefix patch] (re-find #"(\d+\.\d+)\.(\d+)" tag)
             patch            (Long/parseLong patch)
             patch+           (inc patch)]
         (format "%s.%d-%s-SNAPSHOT" prefix patch+ ahead))))}

  :dependencies
  [[org.clojure/clojure "1.10.1" :scope "provided"]
   [org.clojure/clojurescript "1.10.520" :scope "provided"]
   [org.clojars.stumitchell/clairvoyant "0.2.1"]]

  :plugins
  [[me.arrdem/lein-git-version "2.0.3"]]

  :deploy-repositories [["clojars" {:sign-releases false}
                                  :url           "https://clojars.org/repo"
                                  :username      :env/CLOJARS_USERNAME
                                  :password      :env/CLOJARS_PASSWORD]]

  :release-tasks [["deploy" "clojars"]])

