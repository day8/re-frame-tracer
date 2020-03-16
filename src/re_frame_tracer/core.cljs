(ns re-frame-tracer.core
  (:require [clojure.walk :refer [prewalk walk]]
            [clairvoyant.core :refer [ITraceEnter ITraceError ITraceExit]]))

(defn tracer
  "Create custom tracer for Clairvoyant.

  Parameters:
  :color - string, Example: \"#aabbcc\"
  :background - same as color
  :tag - string tag to display before the traced op heading
  :expand - set of op symbols to display expanded by default. Use :bindings to expand all bindings.
  Example: #{'defn 'let :bindings}"
  [& {:keys [color background tag expand] :as options}]
  (let [pr-val (fn pr-val [x] x)
        binding-group (if (contains? expand :bindings)
                        (.-group js/console)
                        (.-groupCollapsed js/console))
        log-binding (fn [form init]
                      (binding-group "%c%s"
                                     "font-weight:bold;" (pr-str form)
                                     (pr-val init)))
        log-exit (fn [exit]
                   (.log js/console "=>" (pr-val exit)))
        has-bindings? #{'fn*
                        `fn
                        'fn
                        'defn
                        `defn
                        'defn-
                        `defn-
                        'defmethod
                        `defmethod
                        'deftype
                        `deftype
                        'defrecord
                        `defrecord
                        'reify
                        `reify
                        'let
                        `let
                        'extend-type
                        `extend-type
                        'extend-protocol
                        `extend-protocol}
        fn-like? (disj has-bindings? 'let `let)]
    (reify
      ITraceEnter
      (-trace-enter
        [_ {:keys [anonymous? arglist args dispatch-val form init name ns op protocol]}]
        (let [group (if (contains? expand (symbol (cljs.core/name op)))
                      (.-group js/console)
                      (.-groupCollapsed js/console))]
          (cond
            (fn-like? op)
            (let [title   (if protocol
                            (str protocol " " name " " arglist)
                            (str ns "/" name
                                 (when tag (str " " tag))
                                 (when dispatch-val
                                   (str " " (pr-str dispatch-val)))
                                 (str " " arglist)
                                 (when anonymous? " (anonymous)")))
                  arglist (remove '#{&} arglist)]
              (group (str "%c" title)
                     (str "color:" color ";"
                          (when background
                            (str "background: " background  ";"
                                 "font-weight: 500; padding: 2px 6px; border-radius: 2px;"))))
              (.group js/console "bindings"))

            (#{'let `let} op)
            (let [title (str op)]
              (group title)
              (.group js/console "bindings"))

            (#{'binding} op)
            (log-binding form init))))

      ITraceExit
      (-trace-exit [_ {:keys [op exit]}]
                   (cond
                     (#{'binding} op)
                     (do (log-exit exit)
                       (.groupEnd js/console))

                     (has-bindings? op)
                     (do (.groupEnd js/console)
                       (log-exit exit)
                       (.groupEnd js/console))))

      ITraceError
      (-trace-error [_ {:keys [op form error ex-data]}]
                    (cond
                      (#{'binding} op)
                      (do
                        (.error js/console (.-stack error))
                        (when ex-data
                          (.groupCollapsed js/console "ex-data")
                          (.groupCollapsed js/console (pr-val ex-data))
                          (.groupEnd js/console)
                          (.groupEnd js/console)))

                      (has-bindings? op)
                      (do (.groupEnd js/console)
                        (do
                          (.error js/console (.-stack error))
                          (when ex-data
                            (.groupCollapsed js/console "ex-data")
                            (.groupCollapsed js/console (pr-val ex-data))
                            (.groupEnd js/console)
                            (.groupEnd js/console)))
                        (.groupEnd js/console)))))))
