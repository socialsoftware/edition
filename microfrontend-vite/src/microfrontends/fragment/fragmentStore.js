import create from 'zustand';

const initialState = {
  diff: false,
  del: false,
  sub: false,
  ins: true,
  note: true,
  fac: false,
};

export const fragmentStore = create(() => ({
  showModal: false,
  docPath: null,
  fragmentNavData: null,
  fragmentSource: null,
  framentExpert: null,
  fragmentVirtual: null,
  expertEditions: null,
  virtualEditions: null,
  sourceInter: null,
  checkboxesState: initialState,
  expertsInter: [],
  virtualsInter: [],
  virtualEditionsAcronyms: [
    'LdoD-JPC-anot',
    'LdoD-Jogo-Class',
    'LdoD-Mallet',
    'LdoD-Twitter',
  ],
}));

export const toggleShow = () =>
  fragmentStore.setState((state) => ({ showModal: !state.showModal }));
export const setDocPath = (path) => fragmentStore.setState({ docPath: path });

export const setFragmentNavData = (data) =>
  fragmentStore.setState({ fragmentNavData: data });

export const setFragmentSource = (data) =>
  fragmentStore.setState({ fragmentSource: data });

export const setFragmentExpert = (data) =>
  fragmentStore.setState({ fragmentExpert: data });

export const setFragmentVirtual = (data) =>
  fragmentStore.setState({ fragmentVirtual: data });

export const setExpertEditions = (data) =>
  fragmentStore.setState({ expertEditions: data });
export const setVirtualEditions = (data) =>
  fragmentStore.setState({ virtualEditions: data });

export const setCheckboxesState = (sel, val) =>
  fragmentStore.setState((state) => ({
    checkboxesState: {
      ...state.checkboxesState,
      [sel]: val,
    },
  }));

export const resetCheckboxesState = () =>
  fragmentStore.setState({ checkboxesState: initialState });

export const setSourceInter = (data) =>
  fragmentStore.setState({
    sourceInter: data,
    expertsInter: [],
    virtualsInter: [],
  });

export const setExpertInter = (inter) =>
  fragmentStore.setState({
    expertsInter: [inter],
    sourceInter: null,
    virtualsInter: [],
  });

export const setVirtualsInter = (inter) =>
  fragmentStore.setState({
    expertsInter: [],
    sourceInter: null,
    virtualsInter: [inter],
  });

export const addSourceToInters = (data) => {
  fragmentStore.setState((state) =>
    state.sourceInter
      ? { sourceInter: null }
      : { sourceInter: data, virtualsInter: [] }
  );
};

export const addToExpertsInter = (inter) =>
  fragmentStore.setState((state) =>
    state.expertsInter.includes(inter)
      ? {
          expertsInter: state.expertsInter.filter((id) => inter !== id),
        }
      : { expertsInter: [...state.expertsInter, inter] }
  );

export const addToVirtualsInter = (inter) =>
  fragmentStore.setState((state) =>
    state.virtualsInter.includes(inter)
      ? {
          virtualsInter: state.virtualsInter.filter((id) => inter !== id),
        }
      : { virtualsInter: [...state.virtualsInter, inter] }
  );

export const getVirtualEditionsAcronyms = () =>
  fragmentStore.getState().virtualEditionsAcronyms;
