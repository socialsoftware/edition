import instance from './axios.js'

export const jwtToken = localStorage.getItem("authorization");

instance.interceptors.request.use(
  function(config) {
    if (jwtToken) {
      config.headers["authorization"] = "Bearer " + jwtToken;
    }
    return config;
  },
  function(err) {
    return Promise.reject(err);
  }
);