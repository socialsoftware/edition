export default /*css*/ `
.fixed-top {
  position: fixed;
  top: 0;
  right: 0;
  left: 0;
  z-index: 1030;
}
.navbar {
  border-top: 10px solid #fc1b27;
}

.container-md {
  padding: 0 20px;
}

.navbar-brand {
  font-weight: 800;
  font-size: 30px;
}
.navbar-toggler {
  display: flex;
  flex-direction: column;
  gap: 5px;
  cursor: pointer;
}
.icon-bar {
  display: block;
  width: 25px;
  height: 3px;
  border-radius: 1px;
  background-color: #333;
}

.navbar-toggler:hover .icon-bar {
  background-color: #0c4ef6;
}
`;
