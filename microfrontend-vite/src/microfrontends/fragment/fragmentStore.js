import create from 'zustand';

const initialState = {
  diff: false,
  del: false,
  sub: false,
  ins: true,
  note: true,
  fac: false,
  line: false,
  align: false,
  pbText: null,
};

export const fragmentStore = create(() => ({
  showModal: false,
  docPath: null,
  fragmentNavData: null,
  fragmentInter: null,
  checkboxesState: initialState,
  authorialsInter: [],
  virtualsInter: [],
  selectedVE: [],
}));

export const toggleShow = () =>
  fragmentStore.setState((state) => ({ showModal: !state.showModal }));
export const setDocPath = (path) => fragmentStore.setState({ docPath: path });

export const setFragmentNavData = (data) =>
  fragmentStore.setState({ fragmentNavData: data });

export const setFragmentInter = (data) =>
  fragmentStore.setState({ fragmentInter: data });

export const setCheckboxesState = (sel, val) =>
  sel === 'fac'
    ? fragmentStore.setState((state) => ({
        checkboxesState: {
          ...state.checkboxesState,
          pbText: null,
          [sel]: val,
        },
      }))
    : fragmentStore.setState((state) => ({
        checkboxesState: {
          ...state.checkboxesState,
          [sel]: val,
        },
      }));

export const resetCheckboxesState = () =>
  fragmentStore.setState({ checkboxesState: initialState });

export const setAuthorialsInter = (inter) => {
  fragmentStore.setState({
    authorialsInter: inter instanceof Array ? [...inter] : [inter],
    virtualsInter: [],
  });
};
export const setVirtualsInter = (inter) =>
  fragmentStore.setState({
    authorialsInter: [],
    virtualsInter: inter instanceof Array ? [...inter] : [inter],
  });

export const addToAuthorialsInter = (inter) => {
  fragmentStore.setState((state) =>
    !state.authorialsInter.includes(inter)
      ? {
          authorialsInter: [...state.authorialsInter.filter(Boolean), inter],
          virtualsInter: [],
        }
      : {
          authorialsInter: state.authorialsInter.filter(
            (id) => id !== inter && Boolean
          ),
          virtualsInter: [],
        }
  );
};

export const addToVirtualsInter = (inter) => {
  fragmentStore.setState((state) =>
    !state.virtualsInter.includes(inter)
      ? {
          virtualsInter: [...state.virtualsInter.filter(Boolean), inter],
          authorialsInter: [],
        }
      : {
          virtualsInter: state.virtualsInter.filter(
            (id) => id !== inter && Boolean
          ),
          authorialsInter: [],
        }
  );
};

export const getAuthorialsInter = () =>
  fragmentStore.getState().authorialsInter;

export const getVirtualsInter = () => fragmentStore.getState().virtualsInter;

export const getCheckboxesState = () =>
  fragmentStore.getState().checkboxesState;

export const getVirtualEditionsAcronyms = () =>
  fragmentStore.getState().selectedVE;

export const setSelectedVE = (veList) =>
  fragmentStore.setState({ selectedVE: veList });

export const fragmentStateSelector = (sel) => fragmentStore(state => state[sel]);