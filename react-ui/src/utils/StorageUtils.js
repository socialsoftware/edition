import { TOKEN } from './Constants';

export function saveToken(token) {
    // TODO: not worrying about the fact that the token is being saved in plain text
    // Probably should encrypt it before saving

    sessionStorage.setItem(TOKEN, token);
}

export function getToken() {
    // TODO: if the token is ever saved while encrypted, this should be the part where it is deccrypted.

    return sessionStorage.getItem(TOKEN);
}

export function deleteToken() {
    sessionStorage.removeItem(TOKEN);
}
