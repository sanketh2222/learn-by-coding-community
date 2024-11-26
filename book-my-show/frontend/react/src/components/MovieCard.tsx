import React from 'react';
import { Star } from 'lucide-react';
import type { Movie } from '../types';

interface MovieCardProps {
  movie: Movie;
  onClick: (movie: Movie) => void;
}

export function MovieCard({ movie, onClick }: MovieCardProps) {
  return (
    <div 
      onClick={() => onClick(movie)}
      className="group cursor-pointer bg-white rounded-lg shadow-md overflow-hidden transition-transform hover:scale-[1.02]"
    >
      <div className="relative aspect-[2/3] overflow-hidden">
        <img 
          src={movie.poster} 
          alt={movie.title}
          className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
        />
        <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/80 to-transparent p-4">
          <div className="flex items-center text-white gap-1">
            <Star className="w-4 h-4 fill-yellow-400 stroke-yellow-400" />
            <span className="text-sm font-medium">{movie.rating}/10</span>
          </div>
        </div>
      </div>
      <div className="p-4">
        <h3 className="font-semibold text-lg text-gray-900 mb-1">{movie.title}</h3>
        <p className="text-sm text-gray-600 mb-2">{movie.language}</p>
        <div className="flex flex-wrap gap-2">
          {movie.genre.map((g) => (
            <span 
              key={g}
              className="px-2 py-1 text-xs bg-gray-100 text-gray-700 rounded-full"
            >
              {g}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
}