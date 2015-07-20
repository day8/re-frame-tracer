# re-frame-tracer

This is a custom tracer for [Clairvoyant](https://github.com/spellhouse/clairvoyant)

This tracer is designed to work well with 
[cljs-devtools](https://github.com/binaryage/cljs-devtools)

## Usage

Add (my private fork of) Clairvoyant to your project `:dependencies`.

[![Clojars Project](http://clojars.org/org.clojars.stumitchell/clairvoyant/latest-version.svg)](http://clojars.org/org.clojars.stumitchell/clairvoyant)

Add re-frame-tracer to your project `:dependencies`.

[![Clojars Project](http://clojars.org/day8/re-frame-tracer/latest-version.svg)](http://clojars.org/day8/re-frame-tracer)

Add the following to your requires clause

    [clairvoyant.core :refer-macros [trace-forms]]
    [re-frame-tracer.core :refer [tracer]]

If you want to wrap your code in a green colour

    (trace-forms {:tracer (tracer :color "green")}
    ;; your code here
    )


