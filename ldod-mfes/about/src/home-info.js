import { sleep, getContainer } from './utils';
const getHomeInfo = () => getContainer()?.querySelector('home-info');

export const showHomeInfo = () => {
	sleep(10).then(() => getHomeInfo() && (getHomeInfo().hidden = false));
};

export const hideHomeInfo = () => {
	getHomeInfo() && (getHomeInfo().hidden = true);
};
