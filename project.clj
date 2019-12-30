(defproject day8/re-frame-tracer "lein-git-inject/version"
  :description "Tracer for clairvoyant that is optimised for cljs-devtools"
  :url "http://github.com/day8/re-frame-tracer"
  :license {:name "MIT"}

  :dependencies
  [[org.clojure/clojure "1.10.1" :scope "provided"]
   [org.clojure/clojurescript "1.10.597" :scope "provided"]
   [org.clojars.stumitchell/clairvoyant "0.2.1"]]

  :plugins      [[day8/lein-git-inject "0.0.4"]
                 [lein-shadow          "0.1.7"]]

  :middleware   [leiningen.git-inject/middleware]

  :deploy-repositories [["clojars" {:sign-releases false
                                    :url           "https://clojars.org/repo"
                                    :username      :env/CLOJARS_USERNAME
                                    :password      :env/CLOJARS_PASSWORD}]]

  :release-tasks [["deploy" "clojars"]])

