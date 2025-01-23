import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState, } from 'react'
import VideoPlayerDetails from '../../components/features/VideoPlayerDetails/VideoPlayerDetails'
import RecommendSeries from '../../components/features/RecommendSeries/RecommendSeries';
import DetailsMovie from '../../components/features/DetailsMovie/DetailsMovie'
import MovieFooter from '../../components/features/FooterDetailsMovie/MovieFooter';
import { getStoredToken, createAuthHeaders } from '../../utils/auth'
const MovieDetailsScreen = ({ }) => {
    const movieId = useParams()
    const navigate = useNavigate();

    useEffect(() => {
        if (!getStoredToken()) {
            navigate('/login');
        }
    }, []);
    return (
        <div>
            <VideoPlayerDetails movieId={movieId} />
            <DetailsMovie movieId={movieId} />
            <RecommendSeries movieId={movieId} />
            <MovieFooter movieId={movieId} />
        </div>)
}
export default MovieDetailsScreen