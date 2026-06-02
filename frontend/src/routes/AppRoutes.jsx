import { Routes, Route } from 'react-router-dom';
import MySalesPage from '../pages/MySales/MySalesPage.jsx';
import MyListsPage from '../pages/MyLists/MyListsPage.jsx';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage.jsx';
import Marketplace from "@/pages/Marketplace/Marketplace.jsx";

export function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<MySalesPage />} />
            <Route path="/my-lists" element={<MyListsPage />} />
            <Route path="/my-library" element={<MyLibraryPage />} />
            <Route path="/marketplace" element={<Marketplace />} />
        </Routes>
    );
}