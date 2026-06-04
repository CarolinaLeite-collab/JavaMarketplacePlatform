import {createContext, useContext, useState} from 'react';
import {setUserId} from '../services/apiClient';

const REAL_USER = 'pedro@aeiou.com';
const GUEST_USER = 'guest@aeiou.com';

const UserContext = createContext({
    currentUser: 'pedro@aeiou.com',
    toggleUser: () => {}
});

export function UserProvider({ children }) {
    const [currentUser, setCurrentUser] = useState(REAL_USER);

    const toggleUser = () => {
        const newUser = currentUser === REAL_USER ? GUEST_USER : REAL_USER;
        console.log('toggling user from:', currentUser, 'to:', newUser);
        setCurrentUser(newUser);
        setUserId(newUser);
    };

    return (
        <UserContext.Provider value={{ currentUser, toggleUser }}>
            {children}
        </UserContext.Provider>
    );
}

export const useUser = () => useContext(UserContext);