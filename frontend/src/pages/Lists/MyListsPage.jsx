import { DefaultLayout } from "../../components/layout/DefaultLayout.tsx";
import { TableList } from "../../components/lists/tablelist/TableList.tsx";
import { Affix } from '@mantine/core';
import { NewListModal } from "../../components/lists/newlistmodal/NewListModal.tsx";
import { useEffect, useState, useContext } from "react";
import { FiltersBar } from "../../components/lists/FiltersBar.tsx";
import { ListsTabs } from "../../components/lists/ListsTabs.tsx";
import AppContext from "../../context/AppContext.tsx";
import { getGenres } from "../../context/lists/ListsActions.jsx";

export default function MyListsPage() {
    const [footerHeight, setFooterHeight] = useState(0);

    // Filters
    const [search, setSearch] = useState("");
    const [genre, setGenre] = useState(null);

    // Genres from backend (your colleagues already fetch them)
    const { state, dispatch } = useContext(AppContext);
    const genres = state.lists.genres;

    useEffect(() => {
        const footer = document.querySelector("footer");
        if (footer) setFooterHeight(footer.offsetHeight);
    }, []);

    useEffect(() => {
        if (state.app.genresHref) {
            getGenres(dispatch, state.app.genresHref);
        }
    }, [state.app.genresHref]);

    return (
        <DefaultLayout title="My Lists" subtitle="CHECK OUT YOUR LISTS:">

            {/* Tabs to switch between My Lists and Public Lists */}
            <ListsTabs />

            {/* Filters Section */}
            <FiltersBar
                search={search}
                onSearchChange={setSearch}
                genre={genre}
                onGenreChange={setGenre}
                genres={genres}
            />

            {/* Table of Lists */}
            <TableList search={search} genre={genre} />

            {/* Floating New List Button */}
            <Affix position={{ bottom: footerHeight + 76, right: 24 }} zIndex={90}>
                <NewListModal />
            </Affix>

        </DefaultLayout>
    );
}
