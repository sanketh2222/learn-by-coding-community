import React, { useState } from 'react';
import { Film, Calendar, MapPin, LogOut } from 'lucide-react';
import { MovieCard } from './components/MovieCard';
import { ShowTimings } from './components/ShowTimings';
import { SeatLayout } from './components/SeatLayout';
import { SearchBar } from './components/SearchBar';
import { MovieFilters } from './components/MovieFilters';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Admin } from './pages/Admin';
import { Vendor } from './pages/Vendor';
import type { Movie, Show, Seat, User } from './types';
import moviesData from './data/movies.json';
import showsData from './data/shows.json';

const LANGUAGES = [
  { id: 'English', label: 'English' },
  { id: 'Hindi', label: 'Hindi' },
  { id: 'Tamil', label: 'Tamil' },
  { id: 'Telugu', label: 'Telugu' },
];

const GENRES = [
  { id: 'Action', label: 'Action' },
  { id: 'Adventure', label: 'Adventure' },
  { id: 'Animation', label: 'Animation' },
  { id: 'Comedy', label: 'Comedy' },
  { id: 'Drama', label: 'Drama' },
  { id: 'Romance', label: 'Romance' },
  { id: 'Sci-Fi', label: 'Sci-Fi' },
];

const generateSeats = (): Seat[] => {
  const seats: Seat[] = [];
  const rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G'];
  
  rows.forEach(row => {
    for (let i = 1; i <= 10; i++) {
      seats.push({
        id: `${row}${i}`,
        row,
        number: i,
        status: Math.random() > 0.8 ? 'booked' : 'available',
        price: 200
      });
    }
  });
  
  return seats;
};

export default function App() {
  const [user, setUser] = useState<User | null>(null);
  const [isRegistering, setIsRegistering] = useState(false);
  const [selectedMovie, setSelectedMovie] = useState<Movie | null>(null);
  const [selectedShow, setSelectedShow] = useState<Show | null>(null);
  const [selectedSeats, setSelectedSeats] = useState<string[]>([]);
  const [seats] = useState<Seat[]>(generateSeats());
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedLanguages, setSelectedLanguages] = useState<string[]>([]);
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
  const [userRole, setUserRole] = useState<'user' | 'admin' | 'vendor'>('user');

  const handleLogin = (email: string) => {
    // Simple role assignment based on email (replace with proper auth)
    if (email.includes('admin')) {
      setUserRole('admin');
    } else if (email.includes('vendor')) {
      setUserRole('vendor');
    } else {
      setUserRole('user');
    }
    setUser({ email, name: email.split('@')[0] });
  };

  const handleRegister = (email: string, name: string) => {
    setUser({ email, name });
  };

  const handleLogout = () => {
    setUser(null);
    setSelectedMovie(null);
    setSelectedShow(null);
    setSelectedSeats([]);
    setUserRole('user');
  };

  const handleSeatSelect = (seatId: string) => {
    setSelectedSeats(prev => 
      prev.includes(seatId) 
        ? prev.filter(id => id !== seatId)
        : [...prev, seatId]
    );
  };

  const handleShowSelect = (show: Show) => {
    setSelectedShow(show);
    setSelectedSeats([]);
  };

  const handleLanguageChange = (language: string) => {
    setSelectedLanguages(prev =>
      prev.includes(language)
        ? prev.filter(l => l !== language)
        : [...prev, language]
    );
  };

  const handleGenreChange = (genre: string) => {
    setSelectedGenres(prev =>
      prev.includes(genre)
        ? prev.filter(g => g !== genre)
        : [...prev, genre]
    );
  };

  const handleClearFilters = () => {
    setSelectedLanguages([]);
    setSelectedGenres([]);
  };

  const filteredMovies = moviesData.movies.filter(movie => {
    const matchesSearch = movie.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      movie.genre.some(g => g.toLowerCase().includes(searchQuery.toLowerCase())) ||
      movie.language.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesLanguage = selectedLanguages.length === 0 || selectedLanguages.includes(movie.language);
    const matchesGenre = selectedGenres.length === 0 || movie.genre.some(g => selectedGenres.includes(g));

    return matchesSearch && matchesLanguage && matchesGenre;
  });

  if (!user) {
    if (isRegistering) {
      return (
        <Register
          onRegister={handleRegister}
          onSwitchToLogin={() => setIsRegistering(false)}
        />
      );
    }
    return (
      <Login
        onLogin={handleLogin}
        onSwitchToRegister={() => setIsRegistering(true)}
      />
    );
  }

  if (userRole === 'admin') {
    return <Admin onLogout={handleLogout} />;
  }

  if (userRole === 'vendor') {
    return <Vendor onLogout={handleLogout} />;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <Film className="w-8 h-8 text-rose-600" />
              <span className="text-xl font-bold text-gray-900">MovieTime</span>
            </div>
            <div className="flex items-center space-x-4">
              <div className="flex items-center text-gray-600">
                <MapPin className="w-5 h-5 mr-1" />
                <span>Mumbai</span>
              </div>
              <div className="flex items-center space-x-2">
                <span className="text-sm text-gray-600">Hi, {user.name}</span>
                <button
                  onClick={handleLogout}
                  className="p-2 text-gray-600 hover:text-gray-900"
                >
                  <LogOut className="w-5 h-5" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {!selectedMovie ? (
          <>
            <div className="flex justify-between items-center mb-8">
              <h2 className="text-2xl font-bold text-gray-900">Recommended Movies</h2>
              <SearchBar value={searchQuery} onChange={setSearchQuery} />
            </div>
            <div className="flex gap-8">
              <MovieFilters
                languages={LANGUAGES}
                genres={GENRES}
                selectedLanguages={selectedLanguages}
                selectedGenres={selectedGenres}
                onLanguageChange={handleLanguageChange}
                onGenreChange={handleGenreChange}
                onClearFilters={handleClearFilters}
              />
              <div className="flex-1">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {filteredMovies.map(movie => (
                    <MovieCard 
                      key={movie.id} 
                      movie={movie} 
                      onClick={setSelectedMovie}
                    />
                  ))}
                </div>
                {filteredMovies.length === 0 && (
                  <div className="text-center py-12">
                    <p className="text-gray-500">No movies found matching your criteria.</p>
                  </div>
                )}
              </div>
            </div>
          </>
        ) : (
          <div>
            <button 
              onClick={() => {
                setSelectedMovie(null);
                setSelectedShow(null);
                setSelectedSeats([]);
              }}
              className="mb-6 text-rose-600 hover:text-rose-700 font-medium"
            >
              ← Back to Movies
            </button>

            <div className="bg-white rounded-lg shadow-md p-6 mb-8">
              <div className="flex gap-6">
                <img 
                  src={selectedMovie.poster} 
                  alt={selectedMovie.title}
                  className="w-48 h-72 object-cover rounded-lg"
                />
                <div>
                  <h1 className="text-3xl font-bold text-gray-900 mb-2">
                    {selectedMovie.title}
                  </h1>
                  <div className="flex items-center gap-2 mb-4">
                    <span className="text-yellow-500">★</span>
                    <span className="font-medium">{selectedMovie.rating}/10</span>
                  </div>
                  <div className="space-y-3 text-gray-600">
                    <p>{selectedMovie.duration} • {selectedMovie.language}</p>
                    <p>{selectedMovie.genre.join(', ')}</p>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-lg shadow-md p-6 mb-8">
              <div className="flex items-center gap-2 mb-4">
                <Calendar className="w-5 h-5 text-gray-600" />
                <h2 className="text-xl font-semibold">Select Show Time</h2>
              </div>
              <ShowTimings 
                shows={showsData.shows.filter(show => show.movieId === selectedMovie.id)}
                onSelectShow={handleShowSelect}
              />
            </div>

            {selectedShow && (
              <div>
                <h2 className="text-xl font-semibold mb-4">Select Seats</h2>
                <SeatLayout
                  seats={seats}
                  selectedSeats={selectedSeats}
                  onSeatSelect={handleSeatSelect}
                />
                
                {selectedSeats.length > 0 && (
                  <div className="fixed bottom-0 left-0 right-0 bg-white shadow-lg border-t">
                    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="text-gray-600">
                            {selectedSeats.length} Seats Selected
                          </p>
                          <p className="text-lg font-bold">
                            Total: ₹{selectedSeats.length * selectedShow.price}
                          </p>
                        </div>
                        <button className="px-8 py-3 bg-rose-600 text-white font-medium rounded-lg hover:bg-rose-700 transition-colors">
                          Book Tickets
                        </button>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            )}
          </div>
        )}
      </main>
    </div>
  );
}