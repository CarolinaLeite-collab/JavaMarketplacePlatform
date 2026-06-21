import {Navigate, Route, Routes} from 'react-router-dom';
import MyListsPage from '../pages/Lists/MyListsPage.jsx';
import PublicListsPage from '../pages/Lists/PublicListsPage.tsx';
import ListItemsPage from '../pages/Lists/ListItemsPage.tsx';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage.jsx';
import Marketplace from "@/pages/Marketplace/Marketplace.jsx";
import ListDetailPage from '../pages/ListDetail/ListDetailPage.tsx';
import {useContext} from 'react';
import AppContext from '../context/AppContext';
import {useUser} from '../context/UserContext';
import AuctionDetailPage from '../pages/AuctionDetail/AuctionDetailPage.jsx';

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

            {/* /lists now redirects straight into My Lists; tabs handle switching to Public */}
            <Route path="/lists" element={<Navigate to="/lists/my-lists" replace />} />

            {/* My Lists */}
            <Route
                path="/lists/my-lists"
                element={
                    <ProtectedRoute href={isLoggedIn ? myListsHref : null}>
                        <MyListsPage />
                    </ProtectedRoute>
                }
            />

            {/* Public Lists */}
            <Route
                path="/lists/public"
                element={<PublicListsPage />}
            />

            {/* List Items Page */}
            <Route
                path="/lists/:listId/items"
                element={<ListItemsPage />}
            />

            {/* My Library */}
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
            <Route path="/auctions/:auctionId" element={<AuctionDetailPage />} />

        </Routes>
    );
}
