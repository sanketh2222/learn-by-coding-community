export interface Movie {
  id: string;
  title: string;
  poster: string;
  rating: number;
  language: string;
  genre: string[];
  duration: string;
}

export interface Show {
  id: string;
  movieId: string;
  time: string;
  price: number;
  seatsAvailable: number;
}

export interface Seat {
  id: string;
  row: string;
  number: number;
  status: 'available' | 'booked' | 'selected';
  price: number;
}

export interface User {
  email: string;
  name: string;
}

export interface Vendor {
  id: string;
  name: string;
  email: string;
  phone: string;
  address: string;
  status: 'active' | 'inactive';
}

export interface Event {
  id: string;
  vendorId: string;
  title: string;
  description: string;
  date: string;
  time: string;
  venue: string;
  category: string;
  price: number;
  capacity: number;
  image: string;
  status: 'upcoming' | 'ongoing' | 'completed' | 'cancelled';
}