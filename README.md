# Sample Om Next Application wih Routing

Om Next app showing an approach to routing and component rendering using secretary

## Overview

This details the approach I'm taking at the moment. The idea is that each route definition sets the
query for the root component and the query results key is then used to decide which subcomponent to
build. The props are also passed onto this component.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 
