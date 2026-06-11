import {Navigate, Route, Routes} from 'react-router-dom';
import MyListsPage from '../pages/MyLists/MyListsPage.jsx';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage.jsx';
import Marketplace from "@/pages/Marketplace/Marketplace.jsx";
import ListDetailPage from '../pages/ListDetail/ListDetailPage.tsx';
import {useContext} from 'react';
import AppContext from '../context/AppContext';
import {useUser} from '../context/UserContext';

function ProtectedRoute({ href, children }) {
    if (!href) return <Navigate to="/" replace />;
    return <>{children}</>;
}

export function AppRoutes() {
    const { state } = useContext(AppContext);
    const { myListsHref, libraryHref } = state.app;
    const { currentUser } = useUser();
    const isLoggedIn = currentUser !== 'guest@aeiou.com';

    return (
        <Routes>
            <Route path="/" element={<Marketplace />} />
            <Route path="/my-lists" element={
                <ProtectedRoute href={isLoggedIn ? myListsHref : null}>
                    <MyListsPage />
                </ProtectedRoute>
            } />
            <Route path="/my-library" element={
                <ProtectedRoute href={isLoggedIn ? libraryHref : null}>
                    <MyLibraryPage />
                </ProtectedRoute>
            } />
            <Route path="/my-lists/:listId" element={
                <ProtectedRoute href={isLoggedIn ? myListsHref : null}>
                    <ListDetailPage />
                </ProtectedRoute>
            } />
        </Routes>
    );
}
