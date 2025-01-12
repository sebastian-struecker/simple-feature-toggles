export const isExpired = (expires_at: number): boolean => {
    const expirationDate = new Date(expires_at * 1000);
    return expirationDate.valueOf() < Date.now();
};

export const parseJwt = (token: string): any => {
    var jwt = require('jsonwebtoken');
    return jwt.decode(token);
};
