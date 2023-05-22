/** @format */

export default {
	virtualEditions: () => '/virtual/virtual-editions',
	virtualEdition: acrn => `/virtual/edition/acronym/${acrn}`,
	manageVirtualEditions: () => '/virtual/manage-virtual-editions',
	category: (acrn, cat) => `/virtual/edition/acronym/${acrn}/category/${cat}`,
	taxonomy: acrn => `/virtual/edition/acronym/${acrn}/taxonomy`,
	user: username => `/virtual/edition/user/${username}`,
	game: (veId, id) => `/virtual/virtual-editions/${veId}/game/${id}`,
	classificationGames: () => '/virtual/classification-games',
};
