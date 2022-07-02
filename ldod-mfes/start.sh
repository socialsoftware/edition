#! /bin/sh

(trap 'kill 0' SIGINT; 
  (cd shell/shared && yarn install && yarn run publish) & 
  (cd shell/client && yarn install && yarn run publish) &
  (cd home && yarn install && yarn run publish) &
  (cd user && yarn install && yarn run publish)
  )
