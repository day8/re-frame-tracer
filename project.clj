(defproject day8/re-frame-tracer "see :git-version below https://github.com/arrdem/lein-git-version"
  :description "Tracer for clairvoyant that is optimised for cljs-devtools"
  :url "http://github.com/day8/re-frame-tracer"
  :license {:name "MIT"}

  :git-version
  {:status-to-version
   (fn [{:keys [tag version branch ahead ahead? dirty?] :as git-status}]
     (if-not (string? tag)
       ;; If git-status is nil (i.e. IntelliJ reading project.clj) then return an empty version.
       "_"
       (if (and (not ahead?) (not dirty?))
         tag
         (let [[_ major minor patch suffix] (re-find #"v?(\d+)\.(\d+)\.(\d+)(-.+)?" tag)]
           (if (nil? major)
             ;; If tag is poorly formatted then return GIT-TAG-INVALID
             "GIT-TAG-INVALID"
             (let [patch' (try (Long/parseLong patch) (catch Throwable _ 0))
                   patch+ (inc patch')]
               (str major "." minor "." patch+ suffix "-" ahead "-SNAPSHOT")))))))}

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

