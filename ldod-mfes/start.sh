(trap 'kill 0' SIGINT; (cd shared && yarn start) & (cd user && yarn start) & (cd home && yarn start) & (cd about && yarn start)& (cd shell && yarn start))
